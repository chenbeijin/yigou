package com.yigou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yigou.dao.ParaMapper;
import com.yigou.dao.SpecMapper;
import com.yigou.dao.TemplateMapper;
import com.yigou.entity.PageResult;
import com.yigou.pojo.goods.Para;
import com.yigou.pojo.goods.Spec;
import com.yigou.pojo.goods.Template;
import com.yigou.service.goods.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TemplateService.class)
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private ParaMapper paraMapper;

    /**
     * 返回全部记录
     *
     * @return
     */
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Template> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Template> templates = (Page<Template>) templateMapper.selectAll();
        return new PageResult<Template>(templates.getTotal(), templates.getResult());
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询条件
     * @return
     */
    public List<Template> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return templateMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Template> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Template> templates = (Page<Template>) templateMapper.selectByExample(example);
        return new PageResult<Template>(templates.getTotal(), templates.getResult());
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param template
     */
    public void add(Template template) {
        templateMapper.insert(template);
    }

    /**
     * 修改
     *
     * @param template
     */
    public void update(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Transactional
    public void delete(Integer id) {
        if (deleteTemplateFindSpecOrParaByTemplateId(id)) {
            throw new RuntimeException("删除模板失败！请删除模板有关的详细和参数。");
        }
        templateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 模板名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }

            // ID
            if (searchMap.get("id") != null) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 规格数量
            if (searchMap.get("specNum") != null) {
                criteria.andEqualTo("specNum", searchMap.get("specNum"));
            }
            // 参数数量
            if (searchMap.get("paraNum") != null) {
                criteria.andEqualTo("paraNum", searchMap.get("paraNum"));
            }
        }
        return example;
    }

    /**
     * 删除时判断该模板是否被信息和参数信息有关 有 则 删除失败抛出异常
     *
     * @param templateId
     * @return
     */
    private boolean deleteTemplateFindSpecOrParaByTemplateId(Integer templateId) {
        HashMap<String, Object> searchMap = new HashMap<>();
        searchMap.put("templateId", templateId);
        // 查询模板是否有详细和参数信息 有 则 删除失败
        Example specExample = new Example(Spec.class);
        Example.Criteria specCriteria = specExample.createCriteria();

        Example paraExample = new Example(Para.class);
        Example.Criteria paraCriteria = paraExample.createCriteria();

        if (searchMap != null) {
            if (searchMap.get("templateId") != null) {
                specCriteria.andEqualTo("templateId", searchMap.get("templateId"));
                paraCriteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
        }

        List<Spec> specs = specMapper.selectByExample(specExample);
        List<Para> paras = paraMapper.selectByExample(paraExample);
        return (specs.size() > 0 || paras.size() > 0);
    }
}
