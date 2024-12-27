package com.ohgiraffers.semiproject.mail.model.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MailDTO {

    private String emailCode;
    private String title;
    private String content;
    private Date sendDate;
    private Date deleteDate;
    private String recipientId;
    private String recipientName;
    private String recipientFolder;
    private String senderId;
    private String senderName;
    private String senderFolder;
    private EmployeeDTO employeeDTO;
    private JobDTO jobDTO;
    private DeptDTO deptDTO;
}
