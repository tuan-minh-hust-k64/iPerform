package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.CheckInRequest;
import com.platform.iperform.common.dto.request.CommentRequest;
import com.platform.iperform.common.dto.response.CommentResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.CheckInStatus;
import com.platform.iperform.dataaccess.comment.adapter.CommentRepositoryImpl;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
import com.platform.iperform.model.CheckIn;
import com.platform.iperform.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class CommentService {
    private final EksDataAccessMapper eksDataAccessMapper;
    private final CommentRepositoryImpl commentRepository;
    private final FunctionHelper functionHelper;
    private final CheckInService checkInService;
    private final SlackService slackService;
    private final HrmsProvider hrmsProvider;

    public CommentService(EksDataAccessMapper eksDataAccessMapper,
                          CommentRepositoryImpl commentRepository,
                          FunctionHelper functionHelper,
                          CheckInService checkInService,
                          SlackService slackService,
                          HrmsV3 hrmsV3
    ) {
        this.eksDataAccessMapper = eksDataAccessMapper;
        this.commentRepository = commentRepository;
        this.functionHelper = functionHelper;
        this.checkInService = checkInService;
        this.slackService = slackService;
        this.hrmsProvider = hrmsV3;
    }
    @Transactional(readOnly = true)
    public CommentResponse getCommentByParentId(CommentRequest commentRequest) {
        List<Comment> result = commentRepository.getCommentByParentId(commentRequest.getParentId())
                .orElse(List.of());
        return CommentResponse.builder()
                .comment(result)
                .build();
    }
    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment result = commentRepository.save(commentRequest.getComment());
        try {
            if(commentRequest.getNotifTo() != null) {
                Map<String, Object> userInfo = hrmsProvider.getManagerInfo(commentRequest.getNotifTo().toString());

//                Map<String, Object> userInfo = functionHelper.getManagerInfo(commentRequest.getNotifTo().toString());
                slackService.sendMessageDM(userInfo.get("email").toString(),
                        "[iPerform] Bạn nhận được comment mới từ manager trên iPerform\n" +
                        "Click ngay vào <https://iperform.ikameglobal.com/#/checkpoint|*ĐÂY*> để đọc comment nhé!\n" +
                        "Vui lòng liên hệ đội ngũ phát triển iPerform nếu không truy cập được link trên!");
            }
        } catch (Exception e) {
            log.error("ERROR: KHông thể gửi thông báo, lỗi HRM");
            throw new RuntimeException("ERROR: KHông thể gửi thông báo, lỗi HRM");
        }
        return CommentResponse.builder()
                .comment(List.of(result))
                .build();
    }
    @Transactional
    public CommentResponse createFeedback(CommentRequest commentRequest) {
        List<Comment> result = commentRepository.saveAll(commentRequest.getComments());
        checkInService.updateCheckIn(CheckInRequest.builder()
                        .checkIn(CheckIn.builder()
                                .status(CheckInStatus.COMPLETED)
                                .lastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")))
                                .id(commentRequest.getComments().get(0).getParentId())
                                .build())
                .build());
        try {
            if(commentRequest.getNotifTo() != null) {
//                Map<String, Object> userInfo = functionHelper.getManagerInfo(commentRequest.getNotifTo().toString());
                Map<String, Object> userInfo = hrmsProvider.getManagerInfo(commentRequest.getNotifTo().toString());
                slackService.sendMessageDM(userInfo.get("email").toString(),
                        "[iPerform] Bạn nhận được comment mới từ manager trên iPerform\n" +
                                "Click ngay vào <https://iperform.ikameglobal.com/#/check-in|*ĐÂY*> để đọc comment nhé!\n" +
                                "Vui lòng liên hệ đội ngũ phát triển iPerform nếu không truy cập được link trên!");
            }
        } catch (IOException e) {
            log.error("ERROR: KHông thể gửi thông báo, lỗi HRM");
            throw new RuntimeException("ERROR: KHông thể gửi thông báo, lỗi HRM");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CommentResponse.builder()
                .comment(result)
                .build();
    }
    @Transactional
    public CommentResponse updateCommentByUserId(CommentRequest commentRequest, UUID userId) {
        CommentEntity commentEntity = commentRepository.findByIdAndUserId(commentRequest.getComment().getId(), userId)
                .orElseThrow(() -> new NotFoundException("Not Found Comment with id: " + commentRequest.getComment().getId()));
        commentEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                commentRequest.getComment(),
                commentEntity,
                functionHelper.getNullPropertyNames(commentRequest.getComment())
        );
        Comment result = commentRepository.save(eksDataAccessMapper.commentEntityToComment(commentEntity));
        return CommentResponse.builder()
                .comment(List.of(result))
                .build();
    }
}
