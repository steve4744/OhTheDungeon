package forge_sandbox.jaredbgreat.dldungeons.rooms;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Level;

import org.bukkit.Bukkit;

/**
 * A list of rooms. This is implemented as a ArrayList, but starting with one
 * for the first room, as zero is used to represented rooms outside the dungeon
 * (the "null room").
 * 
 * @author Jared Blackburn
 *
 */
@SuppressWarnings("serial")
public class RoomList extends ArrayList<Room> {

	private int numRooms;// , index;
	// private Room[] rooms;

	public RoomList(int slots) {
		super(slots);
		add(0, Room.roomNull);
		numRooms = 0;
	}

	/**
	 * The number real rooms actually in the list; the null room is not counted.
	 * 
	 * @return the number of rooms in the list
	 */
	public int realSize() {
		return numRooms;
	}

	/**
	 * Returns true if only the null room is present.
	 * 
	 * @return true if no "real" rooms are in the list
	 */
	public boolean isReallyEmpty() {
		return (numRooms == 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListIterator listIterator() {
		return super.listIterator(1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator iterator() {
		return super.listIterator(1);
	}

	@Override
	public boolean add(Room room) {
		boolean out = super.add(room);
		if (out) {
			numRooms++;
		}
		return out;
	}

	@Override
	public Room remove(int index) {
		Bukkit.getLogger().log(Level.SEVERE,
				"[OTD] WARNING! Trying to remove room from list " + "(Rooms cannot be removed)!");
		return null; // Rooms cannot be removed
	}

}