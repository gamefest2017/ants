package com.ibm.sk.dto.matchmaking;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude="id")
public class Player {
	private final Integer id;
	private final String name;
}
