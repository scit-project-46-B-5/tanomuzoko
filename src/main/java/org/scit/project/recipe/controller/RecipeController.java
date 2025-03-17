package org.scit.project.recipe.controller;

import java.util.UUID;

import org.scit.project.recipe.dto.RecipeConditionDTO;
import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.dto.RecipeUserResponseDTO;
import org.scit.project.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;


    /**
     * CHATGPT에서 받은 recipe 결과를 session에 저장. 
     * recipe 저장 이후의 새로고침을 통한 recipe save를 방지하기 위해 nonce값 session에 저장
     * @param recipeUserRequestDTO
     * @param session
     * @return
    * @throws InterruptedException 
    */
    @PostMapping("/recipe/chatGPT")
    @ResponseBody
    public void viewRecipeOutput(@RequestBody RecipeUserRequestDTO recipeUserRequestDTO, HttpSession session) throws InterruptedException {

        //RecipeUserResponseDTO response = recipeService.getRecipeResponse(recipeUserRequestDTO);

        String title = "가지 꽁치 굴소스 볶음";
        String[] ingredients = {"가지 1개", "꽁치 1마리", "굴소스 2큰술", "곶감 1개"};
        String[] cookingMethods = {"가지를 채 썰어 물기를 제거한다.", 
                                    "꽁치는 소금을 뿌려 10분간 절인 후 물에 헹궈내어 물기를 제거한다.",
                                    "팬에 식용유를 두르고 가지를 볶다가 꽁치를 넣고 함께 볶는다.",
                                    "곶감과 굴소스를 넣고 약불에서 볶아 익힌다.",
                                    "접시에 담아 완성한다."};
        String usage = "일반식";
        String menu = "한식";
        String taste = "매운맛";
        String level = "초보";
        RecipeUserResponseDTO response = RecipeUserResponseDTO.TODTO(title, ingredients, cookingMethods, RecipeConditionDTO.TODTO(usage, menu, taste, level));
        
        session.setAttribute("recipe", response);

        String newUUID = UUID.randomUUID().toString(); 
        session.setAttribute("nonce", newUUID);
        Thread.sleep(2 * 1000);
    }

    /**
     * /recipe/recommend에서 chatGPT request가 성공하여 recipe session 저장 시, 이동하는 페이지 
     * session 값에서 recipe 저장에 필요한 정보를 담아 recipe 정보를 보여주는 페이지 생성
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/recipe/recommend/output")
    public String viewRecipeRecoomendOutput(Model model, HttpSession session) {
        RecipeUserResponseDTO recipeResponse = session.getAttribute("recipe") != null ? (RecipeUserResponseDTO) session.getAttribute("recipe") : RecipeUserResponseDTO.empty();
        String nonce = session.getAttribute("nonce") != null ?  (String) session.getAttribute("nonce") : "";

        model.addAttribute("recipe", recipeResponse);
        model.addAttribute("nonce", nonce);

        return "recipe/recommend_result";
    }

    /**
     * CHATGPT에 recipe recommned를 받기 위한 요청 페이지
     * @param session
     * @return
     */
    @GetMapping("/recipe/recommend")
    public String viewRecipeRecoomend(HttpSession session) {
        session.removeAttribute("recipe"); //해당 페이지 진입 시, 이전 recipe 정보가 아닌 새로운 recipe가 필요하다는 의미이므로 이전 recipe 정보 제거
        return "recipe/recommend";
    }

}
