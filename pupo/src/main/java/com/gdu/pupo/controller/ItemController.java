package com.gdu.pupo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.pupo.domain.ItemDTO;
import com.gdu.pupo.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value="/" , method = {RequestMethod.GET, RequestMethod.POST})
@Controller
public class ItemController {

  private final ItemService itemService;

  @GetMapping("item/list")
  public String list(Model model) {
    List<ItemDTO> itemList = itemService.itemList();
    model.addAttribute("itemList", itemList);
    return "item/list";
  }

  @GetMapping("item/item")
  public String detail(int itemNo, Model model) {
    itemService.itemListByNo(itemNo, model);
    return "item/detail";
  }

  /*
   * @GetMapping("item/detail") public String detail(HttpServletRequest request,
   * Model model) { model.addAttribute("itemDetail",
   * itemService.itemListByNo(request)); return "item/detail"; }
   */
  @GetMapping("seller/main")
  public String sellerMain(Model model) {
    List<ItemDTO> itemList = itemService.itemList();
    model.addAttribute("itemList", itemList);
    return "seller/main";
  }

  @GetMapping("seller/list")
  public String selleList(Model model) {
    List<ItemDTO> itemList = itemService.itemList();
    model.addAttribute("itemList", itemList);
    return "seller/list";
  }

  @GetMapping("seller/register.html")
  public String register() {
    return "seller/register";
  }

  @GetMapping("seller/detail")
  public String sellerDetail(int itemNo, Model model) {
    itemService.itemListByNo(itemNo, model);
    return "seller/detail";
  }
  /*
   * @GetMapping("seller/detail") public String sellerDetail(HttpServletRequest
   * request, Model model) { model.addAttribute("itemDetail",
   * itemService.itemListByNo(request)); return "seller/detail"; }
   */

  @PostMapping("seller/add.do")
  public String add(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {
    int registerResult = itemService.itemRegister(multipartRequest);
    redirectAttributes.addFlashAttribute("registerResult", registerResult);
    return "redirect:/seller/list";
  }

   @PostMapping("seller/modify.html") 
   public String modify(@RequestParam("itemNo") int itemNo, Model model) {
     itemService.itemListByNo(itemNo, model);
     return "seller/modify"; 
   }

   /*
    * @GetMapping("seller/editUpload") public String modify(HttpServletRequest
    * request, Model model) {
    * model.addAttribute("itemDetail",itemService.itemListByNo(request)); return
    * "seller/modify"; }
    */   
  @PostMapping("seller/modify.do")
  public String sellerModify(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {
    int updateResult = itemService.itemUpdate(multipartRequest);
    redirectAttributes.addFlashAttribute("updateResult", updateResult);
    return "redirect:/seller/detail?itemNo=" + multipartRequest.getParameter("itemNo");
  }
  

  @PostMapping("seller/remove.do")
  public String remove(int itemNo, RedirectAttributes redirectAttributes) {
    int deleteResult = itemService.itemDelete(itemNo);
    redirectAttributes.addFlashAttribute("deleteResult", deleteResult);
    return "redirect:/seller/list";
  }
  
  @GetMapping("seller/display")
  public ResponseEntity<byte[]> display(int attachNo){
    return itemService.display(attachNo);
  }
  
  @GetMapping("seller/removeAttach")
  public String removeAttach(int itemNo, int attachNo) {
    itemService.attachDelete(attachNo);
    return "redirect:/seller/detail?itemNo=" + itemNo;
  }
  
}
