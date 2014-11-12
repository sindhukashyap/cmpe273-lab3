package edu.sjsu.cmpe.cache.client;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import edu.sjsu.cmpe.cache.client.ServerFactory;
import edu.sjsu.cmpe.cache.client.DistributedCacheService;
import java.util.Random;
import com.google.common.hash.HashCode;
public class ClientHandler
{

	List<DistributedCacheService> serverList;
    ServerFactory<DistributedCacheService> servers;

	public ClientHandler()
	{
		serverList = new ArrayList<DistributedCacheService>();
        servers = new ServerFactory<DistributedCacheService>(1000);
	}


	public Map<Integer, String> buildObjectsForNodes()
	{

		Map<Integer,String> map = new HashMap<Integer,String>();
         map.put(1,"a");
         map.put(2,"b");
         map.put(3,"c");
         map.put(4,"d");
         map.put(5,"e");
         map.put(6,"f");
         map.put(7,"g");
         map.put(8,"h");
         map.put(9,"i");
         map.put(10,"j");
         return map;

	}

	public void addServersToCircle()
    {
        
        // servers = new ServerFactory<DistributedCacheService>(2,buildServers());
        servers.addAll(this.buildServers());

    }

    public List<DistributedCacheService> buildServers()
    {
        DistributedCacheService cache1 = new DistributedCacheService("http://localhost:3000"); 
        DistributedCacheService cache2 = new DistributedCacheService("http://localhost:3001"); 
        DistributedCacheService cache3 = new DistributedCacheService("http://localhost:3002");
        serverList.add(cache1);
        serverList.add(cache2);
        serverList.add(cache3);
        return serverList;
    }


    public int getBucketNumber(Integer key)
    {

        List<String> servers = new ArrayList<String>();

        for(DistributedCacheService server : serverList)
        {
            servers.add(server.cacheServerUrl);

        }
        HashCode hash = Hashing.md5().hashString(key.toString(), Charsets.UTF_8);
        int bucket = Hashing.consistentHash(hash, servers.size());
        //int bucket = Hashing.consistentHash(Hashing.md5().hashString(key.toString(), Charsets.UTF_8), servers.size());
        //System.out.println("Bucket for  "+node+" is " + servers.get(bucket));
        return bucket;
    }

}
