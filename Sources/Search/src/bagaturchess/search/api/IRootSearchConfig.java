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
package bagaturchess.search.api;


import bagaturchess.bitboard.api.IBoardConfig;


public interface IRootSearchConfig {
	
	
	public static final int TIME_CONTROL_OPTIMIZATION_TYPE_1_1		= 10;
	public static final int TIME_CONTROL_OPTIMIZATION_TYPE_40_40 	= 20;
	
	
	public IBoardConfig getBoardConfig();
	public IEvalConfig getEvalConfig();
	
	public String getSearchClassName();
	public int getMultiPVsCount();
	public ISearchConfig_AB getSearchConfig();
	
	public String getBoardFactoryClassName();
	public String getSemaphoreFactoryClassName();
	
	public int getHiddenDepth();
	
	public int getThreadsCount();
	public int getThreadMemory_InMegabytes();
	
	/**
	 * EGTB Settings
	 */
	public String getGaviotaTbPath();
	public int getGaviotaTbCache();//In megabytes
	
	/**
	 * Memory Settings
	 */
	public int getTimeControlOptimizationType();
	public boolean initCaches();
	public double getTPTUsagePercent();
	public double getTPTQSUsagePercent();
	public double getGTBUsagePercent();
	public double getEvalCacheUsagePercent();
	public double getPawnsCacheUsagePercent();
}
