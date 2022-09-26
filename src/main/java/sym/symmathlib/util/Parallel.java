package sym.symmathlib.util;

import sym.symmathlib.vector.VecTool;

public class Parallel
{
	//parallel run task
	public static void runTask(VecTool.Func1 func1, int count, int threadCount)
	{
		TaskRunner taskRunner = new TaskRunner(func1, count, threadCount);
		taskRunner.execute();
	}
	
	static class TaskRunner
	{
		int totalCount;
		int curCount;
		int threadCount;
		VecTool.Func1 func1;
		Thread[] threadPool;
		
		public TaskRunner(VecTool.Func1 _func1, int _totalCount, int _threadCount)
		{
			func1 = _func1;
			totalCount = _totalCount;
			threadCount = _threadCount;
			curCount = 0;
			threadPool = new Thread[threadCount];
			for(int i = 0; i < threadCount; i++)
			{
				threadPool[i] = new Thread(() ->
				{
					int task = getNextTask();
					while(task >= 0)
					{
						func1.calc(task);
						task = getNextTask();
					}
				});
			}
		}
		
		synchronized int getNextTask()
		{
			if(curCount < totalCount)
			{
				return curCount++;
			}
			else
			{
				return -1;
			}
		}
		
		public void execute()
		{
			try
			{
				for(int i = 0; i < threadCount; i++)
				{
					threadPool[i].start();
				}
				for(int i = 0; i < threadCount; i++)
				{
					threadPool[i].join();
				}
			} catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
