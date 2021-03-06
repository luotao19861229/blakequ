package cm.commons.controller;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cm.commons.controller.form.PortForm;
import cm.commons.controller.form.RouterForm;
import cm.commons.controller.form.RouterLogForm;
import cm.commons.pojos.Port;
import cm.commons.pojos.Router;
import cm.commons.pojos.RouterLog;
import cm.commons.pojos.Station;
import cm.commons.pojos.User;
import cm.commons.stat.service.PortService;
import cm.commons.stat.service.RouterService;
import cm.commons.stat.service.StationService;
import cm.commons.sys.service.RouterLogService;
import cm.commons.util.NullUtil;
import cm.commons.util.PageModel;

@Controller
@RequestMapping("router")
public class RouterController {
	@Autowired
	private StationService stationService;
	
	@Autowired
	private RouterService routerService;
	
	@Autowired
	private PortService portService;
	

	@RequestMapping("show_router_and_port_detail")
	public ModelAndView showRouterDetail(@RequestParam int stationId){
		ModelAndView mv = new ModelAndView();
		System.out.println("*******id:"+stationId);
		RouterForm rf = this.getRouter(stationId);
		mv.addObject("router", rf);
		mv.addObject("routerLog", rf.getRouterLog());
		mv.addObject("ports", rf.getPorts());
		mv.setViewName("StationMonitor/RouterInfo");
		return mv;
	}
	
	@RequestMapping("add_router")
	public ModelAndView addRouter(RouterForm routerForm, BindingResult result, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		if(result.hasErrors()){
			mv.addObject("error", "提交表单失败"+result.getAllErrors());
			mv.setViewName("../public/error");
			return mv;
		}
		Router router = new Router();
		router.setPortCount(routerForm.getPortCount());
		router.setRouterInfo(routerForm.getRouterInfo());
		router.setRouterIp(routerForm.getRouterIp());
		router.setState(routerForm.getState());
		router.setStation((Station)stationService.get(routerForm.getStationId()));
		routerService.saveOrUpdate(router);
		return mv;
	}
	
	
	@RequestMapping("modify_router")
	public ModelAndView modifyRouter(RouterForm routerForm, BindingResult result, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		if(result.hasErrors()){
			mv.addObject("error", "提交表单失败"+result.getAllErrors());
			mv.setViewName("../public/error");
			return mv;
		}
		Router router = (Router) routerService.get(routerForm.getId());
		router.setPortCount(routerForm.getPortCount());
		router.setRouterInfo(routerForm.getRouterInfo());
		router.setRouterIp(routerForm.getRouterIp());
		router.setState(routerForm.getState());
		routerService.saveOrUpdate(router);
		return mv;
	}
	
	/**
	 * 获取路由器信息
	 * @param id
	 * @return
	 */
	private RouterForm getRouter(int stationId){
		Router router = (Router) routerService.getRouterByStationId(stationId);
		RouterForm rf = new RouterForm();
		if(router != null){
			rf.setId(router.getId());
			rf.setPortCount(router.getPortCount());
			rf.setRouterInfo(router.getRouterInfo());
			rf.setRouterIp(router.getRouterIp());
			rf.setState(router.getState());
			
			//routerLog
			Set<RouterLogForm> set = router.getRouterLogs();
			if(set != null && set.size() >0){
				Iterator i = set.iterator();
				RouterLog rl = (RouterLog) i.next();
				RouterLogForm rlf = new RouterLogForm();
				rlf.setCpuRate(rl.getCpuRate());
				rlf.setCurrTime(rl.getCurrTime());
				rlf.setId(rl.getId());
				rlf.setMemRate(rl.getMemRate());
				rlf.setRouterInfo(rl.getRouterInfo());
				rf.setRouterLog(rlf);
			}
			//port
			List<Port> ports = portService.getPortsByRouter(router.getId());
			if(ports != null && ports.size()>0){
				Iterator i = ports.iterator();
				while(i.hasNext()){
					Port p = (Port) i.next();
					PortForm pf = new PortForm();
					pf.setGetTime(p.getGetTime());
					pf.setId(p.getId());
					pf.setIfDescr(p.getIfDescr());
					pf.setIfInOctets(p.getIfInOctets());
					String state = p.getIfOperStatus().toString();
					if(state.startsWith("1")){
						pf.setIfOperStatus("开启状态");
					}else if(state.startsWith("2")){
						pf.setIfOperStatus("关闭状态");
					}
					pf.setIfOutOctets(p.getIfOutOctets());
					pf.setLocIfInBitsSec(p.getLocIfOutBitsSec());
					pf.setLocIfInCrc(p.getLocIfInCrc());
					pf.setLocIfOutBitsSec(p.getLocIfOutBitsSec());
					pf.setPortIp(p.getPortIp());
					Router r = p.getRouter();
					if(r != null){
						pf.setStationName(r.getStation().getName());
					}else{
						pf.setStationName("");
					}
					rf.getPorts().add(pf);
				}
			}
		}
		return rf;
	}
	
	
}
