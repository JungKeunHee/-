package com.ohgiraffers.semiproject.mypage.controller;

import com.ohgiraffers.semiproject.animals.model.dto.AnimalDTO;
import com.ohgiraffers.semiproject.animals.model.service.AnimalsService;
import com.ohgiraffers.semiproject.employee.model.dto.CommentDTO;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeDTOJOB;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeJoinListDTO;
import com.ohgiraffers.semiproject.employee.model.service.EmployeeService;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.mypage.model.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyPageController {

    private final UserInfoService userInfoService;
    private final MyPageService myPageService;
    private final EmployeeService employeeService;
    private final AnimalsService animalsService;

    public MyPageController(UserInfoService userInfoService, MyPageService myPageService, EmployeeService employeeService, AnimalsService animalsService){
        this.userInfoService = userInfoService;
        this.myPageService = myPageService;
        this.employeeService = employeeService;
        this.animalsService = animalsService;
    }

    // 마이페이지 페이지로 이동 및 회원정보 수정
    @GetMapping("/sidemenu/mypage")
    public String mypage(Model model) {
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String empCode = userInfo.getCode();

        EmployeeJoinListDTO info = employeeService.empInfoSelect(empCode);

        model.addAttribute("info", info);

        return "sidemenu/mypage/mypage";
    }

    @PostMapping("/updateProfileImage")
    public String updateProfileImage(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes){
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String empCode = userInfo.getCode();

        boolean updateProfile = myPageService.changeProfileImage(fileName, empCode);

        redirectAttributes.addFlashAttribute("alertMessage", updateProfile ? "프로필 사진이 성공적으로 수정되었습니다." : "프로필 사진 수정에 실패했습니다. 다시 시도해주세요.");

        return "redirect:/sidemenu/mypage";
    }

    @GetMapping("deleteProfileImage")
    public String deleteProfileImage(RedirectAttributes redirectAttributes){
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String empCode = userInfo.getCode();

        boolean deleteProfile = myPageService.deleteProfileImage(empCode);
        redirectAttributes.addFlashAttribute("deleteProfile", deleteProfile);

        return "redirect:/sidemenu/mypage";
    }

    // 마이페이지 - 비밀번호 변경
    @GetMapping("/setting/password")
    public String settingPassWord(){
        return "sidemenu/mypage/settingPass";
    }

    @PostMapping("/setting/password")
    public String settingPassWord(@RequestParam String newPW, @RequestParam String confirmPW, Model model) {
        // 새 비밀번호와 확인 비밀번호가 일치하는지 체크
        if (!newPW.equals(confirmPW)) {
            model.addAttribute("message", "새 비밀번호가 일치하지 않습니다.");

            return "sidemenu/mypage/settingPass";
        }

        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();
        // 새 비밀번호를 변경하는 로직 (예시: 암호화 후 저장)
        boolean passwordChanged = myPageService.changePassword(newPW,code);

        if (passwordChanged) {
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            model.addAttribute("message", "비밀번호 변경에 실패했습니다.");
        }

        return "sidemenu/mypage/settingPass";
    }

    @PostMapping("/check-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkPassword(@RequestBody Map<String, String> request) {
        String enteredPassword = request.get("password");

        // 현재 비밀번호와 입력된 비밀번호 비교
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String currentPassword = userInfo.getPass(); // 저장된 비밀번호 (DB에서 가져온 값)

        boolean isValid = myPageService.checkCurrentPassword(enteredPassword, currentPassword);

        // 결과를 클라이언트로 전달
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isValid);

        return ResponseEntity.ok(response);
    }


    // 마이페이지 - 댓글내역
    @GetMapping("/my-activity/comments")
    public String myComments(Model model) {

        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();
       
        List<CommentDTO> comments = employeeService.getUserComment(code);
        
        model.addAttribute("comments", comments);
        return "sidemenu/mypage/myComments";
    }

    @GetMapping("/my-activity/posts")
    public String myPosts(Model model) {
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();
        List<AnimalDTO> posts = animalsService.getUserPosts(code);

        model.addAttribute("posts", posts);
        return "sidemenu/mypage/myPosts";
    }
}
