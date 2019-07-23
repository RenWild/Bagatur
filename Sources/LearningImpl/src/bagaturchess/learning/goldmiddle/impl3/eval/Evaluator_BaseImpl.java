/**
 *  BagaturChess (UCI chess engine and tools)
 *  Copyright (C) 2005 Krasimir I. Topchiyski (k_topchiyski@yahoo.com)
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
 *  along with BagaturChess. If not, see http://www.eclipse.org/legal/epl-v10.html
 *
 */
package bagaturchess.learning.goldmiddle.impl3.eval;

import bagaturchess.bitboard.impl.Constants;
import bagaturchess.bitboard.impl.Fields;

/**
 * @author i027638
 *
 */
public class Evaluator_BaseImpl {
	
	
	public static final int Direction_NORTH 		= 8;
	public static final int Direction_EAST 			= 1;
	public static final int Direction_SOUTH 		= -Direction_NORTH;//-8
	public static final int Direction_WEST 			= -Direction_EAST;//-1
	public static final int Direction_NORTH_EAST 	= Direction_NORTH + Direction_EAST;//9
	public static final int Direction_SOUTH_EAST 	= Direction_SOUTH + Direction_EAST;//-7
	public static final int Direction_SOUTH_WEST 	= Direction_SOUTH + Direction_WEST;//-9
	public static final int Direction_NORTH_WEST 	= Direction_NORTH + Direction_WEST;//7
	
	public static final long AllSquares 			= ~0;
	public static final long DarkSquares 			= 0xAA55AA55AA55AA55L;
	public static final long LightSquares 			= ~DarkSquares;

	public static final long FileABB = 0x0101010101010101L;
	public static final long FileBBB = FileABB << 1;
	public static final long FileCBB = FileABB << 2;
	public static final long FileDBB = FileABB << 3;
	public static final long FileEBB = FileABB << 4;
	public static final long FileFBB = FileABB << 5;
	public static final long FileGBB = FileABB << 6;
	public static final long FileHBB = FileABB << 7;
	
	public static final long Rank1BB = 0xFF;
	public static final long Rank2BB = Rank1BB << (8 * 1);
	public static final long Rank3BB = Rank1BB << (8 * 2);
	public static final long Rank4BB = Rank1BB << (8 * 3);
	public static final long Rank5BB = Rank1BB << (8 * 4);
	public static final long Rank6BB = Rank1BB << (8 * 5);
	public static final long Rank7BB = Rank1BB << (8 * 6);
	public static final long Rank8BB = Rank1BB << (8 * 7);
	
	public static final int FileA = 0;
	public static final int FileB = 1;
	public static final int FileC = 2;
	public static final int FileD = 3;
	public static final int FileE = 4;
	public static final int FileF = 5;
	public static final int FileG = 6;
	public static final int FileH = 7;
	
	public static final int Rank1 = 0;
	public static final int Rank2 = 1;
	public static final int Rank3 = 2;
	public static final int Rank4 = 3;
	public static final int Rank5 = 4;
	public static final int Rank6 = 5;
	public static final int Rank7 = 6;
	public static final int Rank8 = 7;
	
	public static final int[][] SquareDistance = new int[Fields.H8_ID + 1][Fields.H8_ID + 1];
	public static final long[] SquareBB = new long[Fields.H8_ID + 1];
	public static final long[] FileBB = new long[FileH + 1];
	public static final long[] RankBB = new long[Rank8 + 1];
	public static final long[] AdjacentFilesBB = new long[FileH + 1];
	public static final long[][] ForwardRanksBB = new long[Constants.COLOUR_BLACK + 1][Rank8 + 1];
	public static final long[][] ForwardFileBB = new long[Constants.COLOUR_BLACK + 1][Fields.H8_ID + 1];
	public static final long[][] PawnAttackSpan = new long[Constants.COLOUR_BLACK + 1][Fields.H8_ID + 1];
	public static final long[][] PassedPawnMask = new long[Constants.COLOUR_BLACK + 1][Fields.H8_ID + 1];
	public static final long[][] PawnAttacks = new long[Constants.COLOUR_BLACK + 1][Fields.H8_ID + 1];
	
	
	static {
			
		  for (int squareID = Fields.A1_ID; squareID <= Fields.H8_ID; ++squareID)
		      SquareBB[squareID] = (1 << squareID);
		  
		  for (int fileID = FileA; fileID <= FileH; ++fileID)
		      FileBB[fileID] = fileID > FileA ? FileBB[fileID - 1] << 1 : FileABB;
		  
		  for (int rankID = Rank1; rankID <= Rank8; ++rankID)
			  RankBB[rankID] = rankID > Rank1 ? RankBB[rankID - 1] << 8 : Rank1BB;
		          
		  for (int fileID = FileA; fileID <= FileH; ++fileID)
		      AdjacentFilesBB[fileID] = (fileID > FileA ? FileBB[fileID - 1] : 0) | (fileID < FileH ? FileBB[fileID + 1] : 0);
		  
		  for (int rankID = Rank1; rankID < Rank8; ++rankID)
		      ForwardRanksBB[Constants.COLOUR_WHITE][rankID] = ~(ForwardRanksBB[Constants.COLOUR_BLACK][rankID + 1] = ForwardRanksBB[Constants.COLOUR_BLACK][rankID] | RankBB[rankID]);
		  
	      for (int squareID = Fields.A1_ID; squareID <= Fields.H8_ID; ++squareID)
	      {
	          ForwardFileBB [Constants.COLOUR_WHITE][squareID] = ForwardRanksBB[Constants.COLOUR_WHITE][rank_of(squareID)] & FileBB[file_of(squareID)];
	          PawnAttackSpan[Constants.COLOUR_WHITE][squareID] = ForwardRanksBB[Constants.COLOUR_WHITE][rank_of(squareID)] & AdjacentFilesBB[file_of(squareID)];
	          PassedPawnMask[Constants.COLOUR_WHITE][squareID] = ForwardFileBB[Constants.COLOUR_WHITE][squareID] | PawnAttackSpan[Constants.COLOUR_WHITE][squareID];
	          
	          ForwardFileBB [Constants.COLOUR_BLACK][squareID] = ForwardRanksBB[Constants.COLOUR_BLACK][rank_of(squareID)] & FileBB[file_of(squareID)];
	          PawnAttackSpan[Constants.COLOUR_BLACK][squareID] = ForwardRanksBB[Constants.COLOUR_BLACK][rank_of(squareID)] & AdjacentFilesBB[file_of(squareID)];
	          PassedPawnMask[Constants.COLOUR_BLACK][squareID] = ForwardFileBB[Constants.COLOUR_BLACK][squareID] | PawnAttackSpan[Constants.COLOUR_BLACK][squareID];
	      }
	      
	      for (int squareID1 = Fields.A1_ID; squareID1 <= Fields.H8_ID; ++squareID1)
	    	  for (int squareID2 = Fields.A1_ID; squareID2 <= Fields.H8_ID; ++squareID2)
	              if (squareID1 != squareID2)
	              {
	                  SquareDistance[squareID1][squareID2] = Math.max(distanceFile(squareID1, squareID2), distanceRank(squareID1, squareID2));
	                  //DistanceRingBB[s1][SquareDistance[s1][s2]] |= s2;
	              }
	      
	      int steps[][] = { {}, { 7, 9 }, { 6, 10, 15, 17 }, {}, {}, {}, { 1, 7, 8, 9 } };//For pawns, knight and king only
	      for (int colour = Constants.COLOUR_WHITE; colour <= Constants.COLOUR_BLACK; ++colour)
	    	  for (int squareID = Fields.A1_ID; squareID <= Fields.H8_ID; ++squareID)
	    		  for (int i = 0; i < steps[Constants.TYPE_PAWN].length; ++i) {
	                  
	    			  int squareID_to = squareID + (colour == Constants.COLOUR_WHITE ? steps[Constants.TYPE_PAWN][i] : -steps[Constants.TYPE_PAWN][i]);

	                  if (is_ok(squareID_to) && distance(squareID, squareID_to) < 3)
	                  {
	                      //if (pt == PAWN)
	                          PawnAttacks[colour][squareID] |= squareID_to;
	                      /*else
	                          PseudoAttacks[pt][s] |= to;*/
	                  }
	    		  }
	}
	
	
	public static final long shiftBB(final long b, final int direction)
	{
		switch (direction) {
			case Direction_NORTH:
				return b << 8;
			case Direction_SOUTH:
				return b >>> 8;
			case Direction_EAST:
				return (b & ~FileHBB) << 1;
			case Direction_WEST:
				return (b & ~FileABB) >>> 1;
			case Direction_NORTH_EAST:
				return (b & ~FileHBB) << 9;
			case Direction_NORTH_WEST:
				return (b & ~FileABB) << 7;
			case Direction_SOUTH_EAST:
				return (b & ~FileHBB) >>> 7;
			case Direction_SOUTH_WEST:
				return (b & ~FileABB) >>> 9;
			default:
				throw new IllegalStateException("must be return 0");
		}
	}
	
	
	public static final int file_of(int squareID) {
	  return squareID & 7;
	}
	
	
	public static final int rank_of(int squareID) {
	  return squareID >>> 3;
	}
	
	
	public static final long pawn_attacks_bb(final long b, final int colour) {
	  return colour == Constants.COLOUR_WHITE ?
			  shiftBB(b, Direction_NORTH_WEST) | shiftBB(b, Direction_NORTH_EAST)
			  : shiftBB(b, Direction_SOUTH_WEST) | shiftBB(b, Direction_SOUTH_EAST);
	}
		
		
	/// pawn_attack_span() returns a bitboard representing all the squares that can be
	/// attacked by a pawn of the given color when it moves along its file, starting
	/// from the given square:
	///      PawnAttackSpan[c][s] = forward_ranks_bb(c, s) & adjacent_files_bb(file_of(s));
	public static final long pawn_attack_span(int colour, int squareID) {
		return PawnAttackSpan[colour][squareID];
	}
	
	
	/// forward_file_bb() returns a bitboard representing all the squares along the line
	/// in front of the given one, from the point of view of the given color:
	///      ForwardFileBB[c][s] = forward_ranks_bb(c, s) & file_bb(s)
	public static final long forward_file_bb(int colour, int squareID) {
		return ForwardFileBB[colour][squareID];
	}
	
	
	/// passed_pawn_mask() returns a bitboard mask which can be used to test if a
	/// pawn of the given color and on the given square is a passed pawn:
	///      PassedPawnMask[c][s] = pawn_attack_span(c, s) | forward_file_bb(c, s)
	public static final long passed_pawn_mask(int colour, int squareID) {
		return PassedPawnMask[colour][squareID];
	}
	
	/// adjacent_files_bb() returns a bitboard representing all the squares on the
	/// adjacent files of the given one.
	public static final long adjacent_files_bb(int fileID) {
		return AdjacentFilesBB[fileID];
	}
	
	
	/// relative_rank_bb() takes a color and a rank as input, and returns a bitboard
	/// representing all squares on the given rank from the given color's point of
	/// view.  For instance, relative_rank_bb(WHITE, 7) gives all squares on the
	/// 7th rank, while relative_rank_bb(BLACK, 7) gives all squares on the 2nd
	/// rank.
	/*public static long relative_rank_bb(Color c, Rank r) {
	  return RelativeRankBB[c][r];
	}*/
	
	public static final int relative_rank_byRank(int colour, int rankID) {
		return rankID ^ (colour * 7);
	}

	public static final int relative_rank_bySquare(int colour, int squareID) {
		return relative_rank_byRank(colour, rank_of(squareID));
	}
		
		
	public static final long rank_bb(int squareID) {
		  return RankBB[rank_of(squareID)];
	}
	
	
	public static final int distance(int squareID1, int squareID2) {
		return SquareDistance[squareID1][squareID2];
	}
	
	
	public static final int distanceFile(int squareID1, int squareID2) {
		return distance(file_of(squareID1), file_of(squareID2));
	}
	
	
	public static final int distanceRank(int squareID1, int squareID2) {
		return distance(rank_of(squareID1), rank_of(squareID2));
	}
	
	
	public static final int distanceFileAndRank(int x, int y) {
		return x < y ? y - x : x - y;
	}
	
	
	public static final boolean is_ok(int squareID) {
		return squareID >= Fields.A1_ID && squareID <= Fields.H8_ID;
	}
	
	
	public static void main(String[] args) {
		/*for (int squareID = 0; squareID < 64; squareID++) {
			System.out.println("sqID=" + squareID + ", fileID=" + file_of(squareID) + ", rankID=" + rank_of(squareID));
		}*/
	}
}
