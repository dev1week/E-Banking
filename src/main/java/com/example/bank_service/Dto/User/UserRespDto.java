package com.example.bank_service.Dto.User;

import com.example.bank_service.Domain.User.User;
import lombok.Data;

public class UserRespDto {
    @Data
    public static class JoinResDto{
        private Long id;
        private String username;
        private String fullname;

        public JoinResDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }
}
