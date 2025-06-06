package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.controller.ChargingController.UnlockRequest;
import com.voltunity.evplatform.service.ChargingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChargingController.class)
public class ChargingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChargingService chargingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUnlockSlot_Success() throws Exception {
        UnlockRequest request = new UnlockRequest();
        request.setBookingId(1L);
        request.setStationId(1L);
        request.setSlotId(1L);

        Mockito.when(chargingService.unlockSlot(1L, 1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/api/v1/charges/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Slot desbloqueado com sucesso."));
    }

    @Test
    void testUnlockSlot_Failure() throws Exception {
        UnlockRequest request = new UnlockRequest();
        request.setBookingId(2L);
        request.setStationId(2L);
        request.setSlotId(2L);

        Mockito.when(chargingService.unlockSlot(2L, 2L, 2L)).thenReturn(false);

        mockMvc.perform(post("/api/v1/charges/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não foi possível desbloquear o slot."));
    }

    @Test
    void testUnlockSlot_ExceptionThrown() throws Exception {
        UnlockRequest request = new UnlockRequest();
        request.setBookingId(3L);
        request.setStationId(3L);
        request.setSlotId(3L);

        Mockito.when(chargingService.unlockSlot(anyLong(), anyLong(), anyLong()))
                .thenThrow(new RuntimeException("Erro de negócio"));

        mockMvc.perform(post("/api/v1/charges/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro de negócio"));
    }
}
