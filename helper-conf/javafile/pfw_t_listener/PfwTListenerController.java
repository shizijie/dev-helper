package ;

import com.shizijie.dev.helper.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Administrator
 * @version 2020-08-15 18:01:48
 */
@RestController
@Slf4j
public class PfwTListenerController extends BaseController{
    @Autowired
    private PfwTListenerService PfwTListenerService;

}
