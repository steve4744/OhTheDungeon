/* 
 * Copyright (C) 2021 shadow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package otd.lib.async.later.roguelike;

import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import org.bukkit.Chunk;

/**
 *
 * @author
 */
public abstract class Later {

	public abstract Coord getPos();

	public abstract void doSomething();

	public abstract void doSomethingInChunk(Chunk c);

	protected int x, y, z;

	public void setOffset(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Coord getPosWithOffset() {
		Coord pos = new Coord(getPos());
		pos.add(new Coord(x, y, z));
		return pos;
	}
}
