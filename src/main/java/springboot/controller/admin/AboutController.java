package springboot.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.model.bo.RestResponseBo;
import springboot.model.vo.MetaVo;
import springboot.service.IMetaService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zww
 * @date 2018/3/25 12:32
 */
@Controller
@RequestMapping("admin/about")
public class AboutController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @Resource
    private IMetaService metaService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaVo> metas = metaService.getMetas(Types.ATTACH_URL.getType());
        request.setAttribute("about", metas);
        return "admin/about";
    }
}
