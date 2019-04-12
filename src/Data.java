import java.util.ArrayList;

import javax.swing.event.*;

public class Data {
	
	//who's turn is it
	private boolean isPlayerA;
	private boolean gameEnd;
	private boolean canUndo;
	
	//the pits(0-5) and the mancala(6) 
	private int[] playerA;
	private int[] playerB;
	
	private int[] undoA;
	private int[] undoB;
	
	private ArrayList<ChangeListener> listeners;
	
	/**
	 * Constructs a new Data object, filling each pit with the specified amount of stones
	 * @param stones the number of stones per pit
	 */
	public Data(int stones)
	{
		playerA = new int[7];
		playerB = new int[7];
		listeners = new ArrayList<>();
		isPlayerA = true;
		gameEnd = false;
		for(int i = 0; i < 6; i++)
		{
			playerA[i] = stones;
			playerB[i] = stones;
		}
		undoA = playerA;
		undoB = playerB;
	}
	
	
	public void select(int pit)
	{
		int[] side = playerB;
		if(isPlayerA)
		{
			side = playerA;
		}
		
		int numOfStones = side[pit];
		int counter = 7;
		side[pit] = 0;
		pit++;
		
		while(numOfStones > 0)
		{
			while(pit < counter && numOfStones > 0)
			{
				side[pit]++;
				numOfStones--;
				pit++;
			}
			if(pit == counter && numOfStones > 0)
			{
				if(counter == 7)
				{
					counter = 6;
				}
				else
				{
					counter = 7;
				}
				
				if(side == playerA)
				{
					side = playerB;
				}
				else
				{
					side = playerA;
				}
				pit = 0;
			}
		}
		//if we're on the current player's side
		if(counter == 7)
		{
			if(pit < counter && side[pit-1] == 1)
			{
				if(side == playerA)
				{
					capture(side, playerB, pit);
				}
				else if(side == playerB)
				{
					capture(side, playerA, pit);
				}
			}
			//if the last stone goes into current player's mancala
			else if(pit >= 7 && side[pit-1] == 1)
			{
				isPlayerA = !isPlayerA;
			}
			isPlayerA = !isPlayerA;
		}
		
		if(isEmpty(playerA))
		{
			gameEndProtocal(playerB);
		}
		else if(isEmpty(playerB))
		{
			gameEndProtocal(playerA);
		}
		
		
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener l: listeners)
		{
			l.stateChanged(event);
		}
	}
	
	
	/**
	 * Adds a changelistener to the listeners of the data class
	 * @param listener the listener to add
	 */
	public void addChangeListener(ChangeListener listener)
	{
		listeners.add(listener);
	}
	
	
	/**
	 * Gets value of isPlayerA
	 * @return boolean isPlayerA 
	 */
	public boolean getIsPLayerA()
	{
		return isPlayerA;
	}
	
	public void capture(int[] cSide, int[] oppSide, int pit)
	{
		cSide[pit-1] = 0;
		int addStone = oppSide[6-pit] + 1;
		oppSide[6-pit] = 0;
		cSide[6] += addStone;
	}
	
	public void gameEndProtocal(int[] nonEmpty)
	{
		for(int i = 0; i < nonEmpty.length-1; i++)
		{
			nonEmpty[nonEmpty.length-1] += nonEmpty[i];
			nonEmpty[i] = 0;
		}
		gameEnd = true;
	}

	public boolean isEmpty(int[] test)
	{
		boolean empty = true;
		int counter = 0;
		while(empty && counter < test.length-1)
		{
			if(test[counter] != 0 )
			{
				empty = false;
			}
		}
		return empty;
	}
	
}