package com.vk.article.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApRejection;
import com.vk.article.domain.table.ApRejectionTableDef;
import com.vk.article.domain.vo.RejectionListVo;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.mapper.ApRejectionMapper;
import com.vk.article.service.ApArticleService;
import com.vk.article.service.ApRejectionService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.vk.article.domain.table.ApRejectionTableDef.AP_REJECTION;

@RestController
@RequestMapping("/rejection")
public class ApRejectionController {

    @Autowired
    private ApRejectionService apRejectionService;

    @Autowired
    private ApArticleService apArticleService;
   @GetMapping("/auditList")
    public AjaxResult getAuditList(@RequestParam(value = "id") Long id){
       List<RejectionListVo> vo=apRejectionService.listAs(
               QueryWrapper.create().where(AP_REJECTION.ARTICLE_ID.eq(id)).orderBy(AP_REJECTION.CREATED_TIME,true)
               ,RejectionListVo.class);
       return AjaxResult.success(vo);
   }


    @PostMapping("/rejection")
    public AjaxResult addRejection(
            @RequestBody ApRejection rejection
    ){
        Long articleId = rejection.getArticleId();
        if (StringUtils.isLongEmpty(articleId)){
            throw new LeadNewsException("文章错误");
        }

        String reject = rejection.getRejection();
        if (StringUtils.isEmpty(reject)){
            throw new LeadNewsException("理由不能为空");
        }
        ApRejection apRejection = new ApRejection();
        apRejection.setArticleId(articleId);
        apRejection.setRejection(reject);
        apRejection.setProhibited(rejection.getProhibited());
        apRejection.setCreatedTime(LocalDateTime.now());

        ApArticle apArticleById = apArticleService.getById(articleId);
        if (ObjectUtils.isEmpty(apArticleById)){
            throw new LeadNewsException("文章错误");
        }
        ApArticle apArticle = new ApArticle();
        apArticle.setId(articleId);
        apArticle.setStatus(3);
        Db.tx(()->{
            try {
                apRejectionService.save(apRejection);
                apArticleService.updateById(apArticle);
            }catch (Exception e){
                return false;
            }
            return true;
        });
        return AjaxResult.success();
    }


    @PutMapping("/passRejection")
    public AjaxResult passRejection(
            @RequestParam(value = "id") Long id
    ){
        ApArticle apArticleById = apArticleService.getById(id);
        if (ObjectUtils.isEmpty(apArticleById)){
            throw new LeadNewsException("文章错误");
        }

        ApArticle apArticle = new ApArticle();
        apArticle.setId(id);
        apArticle.setStatus(8);

        apArticleService.updateById(apArticle);
        return AjaxResult.success();
    }
}
