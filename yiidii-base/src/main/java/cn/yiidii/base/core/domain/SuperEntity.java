package cn.yiidii.base.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 包括id、create_time、created_by字段的表继承的基础实体
 *
 * @author zuihou
 */
@SuperBuilder
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SuperEntity<T> implements Serializable {

    public static final String FIELD_ID = "id";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_TIME_COLUMN = "create_time";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_BY_COLUMN = "created_by";

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    protected T id;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    protected T createdBy;

}
