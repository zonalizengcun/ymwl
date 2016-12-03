package com.yim.pix.world.entity.battle;

/**
 * 房间
 * @author admin
 *
 */
public class Room {

	private int roomId;
	
	private int player1Id;
	
	private int player2Id;
	
	/**
	 * 红方
	 */
	private Square redSquare;
	
	/**
	 * 蓝方
	 */
	private Square blueSquare;
	
	public Room(int roomId) {
		this(roomId,0,0);
	}
	
	public Room(int roomId,int player1Id,int player2Id){
		this.roomId = roomId;
		this.player1Id = player1Id;
		this.player2Id = player2Id;
		this.redSquare = new Square();
		this.blueSquare = new Square();
		this.init();
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getPlayer1Id() {
		return player1Id;
	}

	public void setPlayer1Id(int player1Id) {
		this.player1Id = player1Id;
	}

	public int getPlayer2Id() {
		return player2Id;
	}

	public void setPlayer2Id(int player2Id) {
		this.player2Id = player2Id;
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
	
}
