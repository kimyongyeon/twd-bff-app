package com.twd.bff.app;

import com.google.gson.Gson;
import com.twd.bff.app.biz.v1.sample.controller.SampleController;
import com.twd.bff.app.biz.v1.sample.vo.SampleVO;
import com.twd.bff.app.common.vo.ApiMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_CODE;
import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_MSG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ActiveProfiles(value = "local")
@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
@Slf4j
class TwdBffAppApplicationTests {

//    @Autowired
//    SampleFeign sampleFeign;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

//    @Test
//    void testFeign() {
//        this.sampleFeign.status(200);
//    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 한글 깨짐 처리
                .build();
    }

    @Test
    void testRest() throws Exception {

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        Gson gson = new Gson();

        ApiMessageVO apiMessageVO = ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(SampleVO.builder().build())
                .respCode(OK_RESP_CODE)
                .build();


        mockMvc.perform(get("/api/v1/sample/get")       // 1, 2
                .params(info))              // 3
                .andExpect(status().isOk())     // 4
                .andExpect(content().string(gson.toJson(apiMessageVO)))
                .andDo(print());

    }



}
