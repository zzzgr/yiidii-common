package cn.yiidii.base.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 包括name、parent_id、sort 字段的表继承的树形实体
 *
 * @author zuihou
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class TreeEntity<E, T extends Serializable> extends Entity<T> {

    /**
     * 名称
     */
    @TableField(value = "name")
    protected String name;

    /**
     * 父ID
     */
    @TableField(value = "parent_id")
    protected T parentId;

    /**
     * 排序
     */
    @TableField(value = "sort")
    protected Integer sort;

    @TableField(exist = false)
    protected List<E> children;


    /**
     * 初始化子类
     */
    public void initChildren() {
        if (getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
    }
}
