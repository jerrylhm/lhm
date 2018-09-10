package com.lhm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lhm.constant.HandleEnum;
import com.lhm.dao.AdDao;
import com.lhm.dto.AdDto;
import com.lhm.entity.Ad;
import com.lhm.entity.Page;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private AdDao adDAO;
	/**
	 * 首页 —— 广告（超值特惠）
	 */
	@RequestMapping(value = "/homead", method = RequestMethod.GET)
	public List<AdDto> homead() {
		AdDto adDto = new AdDto();
		adDto.setLink("你的吗");
		List<AdDto> ls = new ArrayList<AdDto>();
		ls.add(adDto);
		return ls;
	}
	
	@RequestMapping(value = "/homead/{id}/{type}", method = RequestMethod.GET,produces="text/html; charset=UTF-8")
	public String search(@PathVariable("id") int id, @PathVariable("type") String type) {
		Page page = new Page();
		page.setPageNumber(2);
		page.setCurrentPage(2);
		System.out.println(adDAO.queryByLike("a", page));
//		Ad ad = new Ad();
//		ad.setTitle("faker");
//		System.out.println(adDAO.insert(ad));
		//添加完数据后mybits自动把自增id填入
//		System.out.println(ad.getId());
		return "草泥马的比";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") int id) {
		System.out.println("即将删除的id:" + id);
		return "草泥马的比";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String put(@PathVariable("id") int id,MultipartFile file) {
		System.out.println("即将修改的id:" + id);
		System.out.println("即将上传的文件名:" + file.getOriginalFilename());
		return "草泥马的比";
	}
}
