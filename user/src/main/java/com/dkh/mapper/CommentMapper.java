package com.dkh.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dkh.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dkh
 * @since 2023-01-18
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    public List<Comment> getCommentList(@Param("current") Integer current, @Param("size") Integer size);
}
