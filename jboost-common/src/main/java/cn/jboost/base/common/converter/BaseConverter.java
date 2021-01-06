package cn.jboost.base.common.converter;

import java.util.Collection;

/**
 * bean 与 bean之间的转换
 * 子接口继承该接口，并加 @Mapper 注解，如：
 * @Mapper(componentModel = BaseAdapter.SPRING)
 * public interface DemoAdapter extends BaseConvertor<Demo, DemoDto>
 * @param <T> bean type  Serializable
 * @param <D> bean type Serializable
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public interface BaseConverter<T, D> {

    String SPRING = "spring";

    /**
     * entity 转 DTO
     * @param entity
     * @return
     */
    D toDTO(T entity);

    /**
     * DTO 转 entity
     * @param dto
     * @return
     */
    T toEntity(D dto);

    /**
     * entity集合转DTO集合
     * @param entities
     * @return
     */
    Collection<D> toDTO(Collection<T> entities);

    /**
     * DTO集合转entity集合
     * @param dtos
     * @return
     */
    Collection<T> toEntity(Collection<D> dtos);
}
