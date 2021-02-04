package com.yigou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yigou.dao.CategoryMapper;
import com.yigou.entity.PageResult;
import com.yigou.pojo.goods.Category;
import com.yigou.service.goods.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 返回全部记录
     *
     * @return
     */
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Category> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Category> categorys = (Page<Category>) categoryMapper.selectAll();
        return new PageResult<Category>(categorys.getTotal(), categorys.getResult());
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询条件
     * @return
     */
    public List<Category> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return categoryMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Category> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Category> categorys = (Page<Category>) categoryMapper.selectByExample(example);
        return new PageResult<Category>(categorys.getTotal(), categorys.getResult());
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param category
     */
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    /**
     * 修改
     *
     * @param category
     */
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        if (deleteCategoryFindParentId(id)) {
            throw new RuntimeException("删除分类失败！该分类存在下级分类。");
        }
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 分类名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 是否显示
            if (searchMap.get("isShow") != null && !"".equals(searchMap.get("isShow"))) {
                criteria.andLike("isShow", "%" + searchMap.get("isShow") + "%");
            }
            // 是否导航
            if (searchMap.get("isMenu") != null && !"".equals(searchMap.get("isMenu"))) {
                criteria.andLike("isMenu", "%" + searchMap.get("isMenu") + "%");
            }

            // 分类ID
            if (searchMap.get("id") != null) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 商品数量
            if (searchMap.get("goodsNum") != null) {
                criteria.andEqualTo("goodsNum", searchMap.get("goodsNum"));
            }
            // 排序
            if (searchMap.get("seq") != null) {
                criteria.andEqualTo("seq", searchMap.get("seq"));
            }
            // 上级ID
            if (searchMap.get("parentId") != null) {
                criteria.andEqualTo("parentId", searchMap.get("parentId"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }

        }
        return example;
    }

    /**
     * 删除时查询该分类是否和其他分类有关 有 则删除失败 抛出异常
     *
     * @param parentId
     * @return
     */
    private boolean deleteCategoryFindParentId(Integer parentId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("parentId", parentId);
        List<Category> categories = categoryMapper.selectByExample(createExample(searchMap));
        return categories.size() > 0;
    }

}
