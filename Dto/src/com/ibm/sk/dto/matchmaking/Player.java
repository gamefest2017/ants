package com.ibm.sk.dto.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude="id")
@AllArgsConstructor
@NoArgsConstructor
public class Player {
	private Integer id;
	private String name;
}
