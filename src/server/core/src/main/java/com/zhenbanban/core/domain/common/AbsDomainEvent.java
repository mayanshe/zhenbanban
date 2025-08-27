package com.zhenbanban.core.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhenbanban.core.infrastructure.util.ApplicationContextHolder;
import com.zhenbanban.core.infrastructure.util.HttpUtils;
import com.zhenbanban.core.infrastructure.util.StrUtils;
import com.zhenbanban.core.shared.valueobj.UserClaims;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;

/**
 * 领域公共：抽象领域事件基类
 *
 * @author zhangxihai 2025/08/01
 */
@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbsDomainEvent implements Serializable {

    @JsonIgnore
    private Long refId;                  // 关联ID（如用户ID、订单ID等）

    @JsonIgnore
    private String eventType;            // 事件类型

    @JsonIgnore
    private String eventId;              // 事件ID（唯一标识）

    @JsonIgnore
    private Long occurredAt;             // 事件发生时间戳

    @JsonIgnore
    @Builder.Default
    private Short state = 0;             // 事件状态: 0-未处理, 1-已处理, 2-已取消

    private String createdBy;             // 创建者ID

    public String getEventType() {
        return this.getClass().getSimpleName();
    }

    public String getEventId() {
        String eventClassName = this.toString();
        String xcsrfToken = HttpUtils.getXCSRFToken();
        String md5 = StrUtils.convertToMd5(eventClassName + xcsrfToken);
        return eventId != null ? eventId : String.format("domain:%s", md5);
    }

    public Long getOccurredAt() {
        return occurredAt != null ? occurredAt : System.currentTimeMillis();
    }

    public Short getState() {
        return state != null ? state : 0;
    }

    public void setCreatedBy(String createdBy) {
        if (createdBy.equals("current")) {
            try {
                UserClaims userClaims = ApplicationContextHolder.getContext().getBean(UserClaims.class);
                this.createdBy = userClaims.toJsonString();
            } catch (Exception e) {
                UserClaims userClaims = UserClaims.builder()
                        .id(0L)
                        .username("anonymous")
                        .fullname("未知用户")
                        .scope("unknown")
                        .build();
                this.createdBy = userClaims.toString();
            }

            return;
        }

        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy != null && !createdBy.isEmpty() ? createdBy : "system:0:error";
    }

}
