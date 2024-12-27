package com.ohgiraffers.semiproject.animals.model.dto;

import lombok.*;


import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InventoryDTO {

    private String itemCode;
    private String itemType;
    private String itemGroup;
    private String dealName;
    private String dealUrl;
    private Date dealDate;
    private String itemName;
    private int itemNum;
    private String typeCode;
    private TypeDTO typeDTO;

}
