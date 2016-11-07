package com.yim.pix.world.entity.battle;

import com.yim.pix.world.entity.Player;

/**
 * 房间
 * @author admin
 *
 */
public class Room {

	private int roomId;
	
	private Player player1;
	
	private Player player2;
	
	/**
	 * 红方
	 */
	private Square redSquare;
	
	/**
	 * 蓝方
	 */
	private Square blueSquare;
	
	public Room(int roomId) {
		this(roomId,null,null);
	}
	
	public Room(int roomId,Player player1,Player player2){
		this.roomId = roomId;
		this.setPlayer1(player1);
		this.setPlayer2(player2);
		this.redSquare = new Square(player1);
		this.blueSquare = new Square(player2);
		this.init();
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Square getRedSquare() {
		return redSquare;
	}

	public void setRedSquare(Square redSquare) {
		this.redSquare = redSquare;
	}

	public Square getBlueSquare() {
		return blueSquare;
	}

	public void setBlueSquare(Square blueSquare) {
		this.blueSquare = blueSquare;
	}
	
	//初始化房间
	private void init() {
		
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}
	
}
