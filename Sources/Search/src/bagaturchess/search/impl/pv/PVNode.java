/*
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
 *  
 *  Open Source project location: http://sourceforge.net/projects/bagaturchess/develop
 *  SVN repository https://bagaturchess.svn.sourceforge.net/svnroot/bagaturchess
 *
 *  This file is part of BagaturChess program.
 * 
 *  BagaturChess is open software: you can redistribute it and/or modify
 *  it under the terms of the Eclipse Public License version 1.0 as published by
 *  the Eclipse Foundation.
 *
 *  BagaturChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Eclipse Public License for more details.
 *
 *  You should have received a copy of the Eclipse Public License version 1.0
 *  along with BagaturChess. If not, see <http://www.eclipse.org/legal/epl-v10.html/>.
 *
 */
package bagaturchess.search.impl.pv;


import java.util.List;
import bagaturchess.bitboard.impl.movegen.MoveInt;


public class PVNode {
	
	
	public PVNode parent;
	public PVNode child;
	
	public int eval;
	public int bestmove;
	public boolean leaf;
	public boolean nullmove;
	
	
	public PVNode() {
		bestmove = 0;
		leaf = true;
	}
	
	
	private static void extractPV(PVNode res, List<Integer> result) {
		PVNode cur = res;
		while(cur != null && (cur.bestmove != 0 || cur.nullmove)) {
			result.add( cur.nullmove ? 0 : cur.bestmove);
			cur = cur.child;
			if (cur != null && cur.leaf) {
				break;
			}
		}
	}
	
	
	public static int[] convertPV(PVNode line, List<Integer> buff) {
		
		extractPV(line, buff);
		
		int[] result = new int[buff.size()];
		for (int i=0; i<result.length; i++) {
			result[i] = buff.get(i);
		}
		return result;
	}
	
	
	@Override
	public String toString() {
		String result = "";
		
		PVNode cur = this;
		while(cur != null && (cur.bestmove != 0 || cur.nullmove)) {
			result += cur.nullmove ? "OOOO, " : MoveInt.moveToString(cur.bestmove) + ", ";
			cur = cur.child;
		}
		
		return result;
	}
}

