package cn.yiidii.base.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 包括id、create_time、created_by、updated_by、update_time字段的表继承的基础实体
 *
 * @author zuihou
 */
@SuperBuilder(builderMethodName = "entityBuilder")
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class Entity<T> extends SuperEntity<T> {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATED_BY = "updatedBy";
    public static final String UPDATE_TIME_COLUMN = "update_time";
    public static final String UPDATED_BY_COLUMN = "updated_by";


    @TableField(value = "status")
    protected Integer status;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    protected T updatedBy;

    public Entity(T id, LocalDateTime createTime, T createdBy, LocalDateTime updateTime, T updatedBy) {
        super(id, createTime, createdBy);
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
    }

    public Entity() {
    }

}
