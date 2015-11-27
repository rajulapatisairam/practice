package com.practice.core.interfaces;

/**
 * 
 * Multimedia is an interface,
 * which defines common functionalities for disk devices.
 *
 */
interface Multimedia
{
	public void playVideo();
	public void playSound();
	public void pause();
}

class CD implements Multimedia{

	@Override
	public void playVideo() {
		System.out.println(" CD Play Video ");
	}

	@Override
	public void playSound() {
		System.out.println(" CD Play Sound ");
		
	}

	@Override
	public void pause() {
		System.out.println(" CD In Pause Mode !");
	}

	
}

class DVD implements Multimedia{

	@Override
	public void playVideo() {
		System.out.println(" DVD Play Video ");
	}

	@Override
	public void playSound() {
		System.out.println(" DVD Play Sound ");
		
	}

	@Override
	public void pause() {
		System.out.println(" DVD In Pause Mode !");
	}

	
}

class DVDPlayer{
	public void insertDISK(Multimedia disk){
		disk.playVideo();
		disk.playSound();
		disk.pause();
	}
}
public class InterfacePractice {

	public static void main(String[] args) {
          DVDPlayer player = new DVDPlayer();
          CD cd = new CD();
          DVD dvd = new DVD();
          System.out.println("\n Inset CD");
          player.insertDISK(cd);
          System.out.println("\n Inset DVD");
          player.insertDISK(dvd);
	}

}
