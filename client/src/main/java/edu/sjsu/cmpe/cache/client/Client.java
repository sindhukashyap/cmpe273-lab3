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

public class Client 
{

    // List<DistributedCacheService> serverList;
    // ServerFactory<DistributedCacheService> servers;
    ClientHandler handler;

    private Client()
    {
        // serverList = new ArrayList<DistributedCacheService>();
        // servers = new ServerFactory<DistributedCacheService>(1000);
        handler = new ClientHandler();
        
        //this.addServersToCircle();
        handler.addServersToCircle();
        Map<Integer,String> map = handler.buildObjectsForNodes();
        for (Integer key : map.keySet())
         {
            int bucketNumber = handler.getBucketNumber(key);
            //int bucketNumber = this.getBucketNumber(key);
            DistributedCacheService server = handler.serverList.get(bucketNumber);
            //DistributedCacheService server = serverList.get(bucketNumber);
            server.put(key, map.get(key));
             System.out.println("Key "+key+" goes to server "+ bucketNumber);
            //System.out.println("get  => " + value);
         }

         this.getValuesAndPrintOnConsole(map);


    }

    public void getValuesAndPrintOnConsole(Map<Integer,String> map)
    {

         for (Integer key : map.keySet())
         {
            int bucketNumber = handler.getBucketNumber(key);
            //int bucketNumber = this.getBucketNumber(key);
            DistributedCacheService server = handler.serverList.get(bucketNumber);
            //DistributedCacheService server = serverList.get(bucketNumber);
            String value = server.get(key);
            System.out.println("Key: "+key+", Value: "+value);
            //System.out.println("get  => " + value);
         }
    }

    public static void main(String[] args) throws Exception 
    {
        
        System.out.println("Starting Cache Client...");
        new Client();
        System.out.println("Exiting Cache Client...");
    }       

    // public void addServersToCircle()
    // {
        
    //     // servers = new ServerFactory<DistributedCacheService>(2,buildServers());
    //     servers.addAll(this.buildServers());

    // }

    // public List<DistributedCacheService> buildServers()
    // {
    //     DistributedCacheService cache1 = new DistributedCacheService("http://localhost:3000"); 
    //     DistributedCacheService cache2 = new DistributedCacheService("http://localhost:3001"); 
    //     DistributedCacheService cache3 = new DistributedCacheService("http://localhost:3002");
    //     serverList.add(cache1);
    //     serverList.add(cache2);
    //     serverList.add(cache3);
    //     return serverList;
    // }


    // public int getBucketNumber(Integer key)
    // {

    //     List<String> servers = new ArrayList<String>();

    //     for(DistributedCacheService server : serverList)
    //     {
    //         servers.add(server.cacheServerUrl);

    //     }
    //     HashCode hash = Hashing.md5().hashString(key.toString(), Charsets.UTF_8);
    //     int bucket = Hashing.consistentHash(hash, servers.size());
    //     //int bucket = Hashing.consistentHash(Hashing.md5().hashString(key.toString(), Charsets.UTF_8), servers.size());
    //     //System.out.println("Bucket for  "+node+" is " + servers.get(bucket));
    //     return bucket;
    // }

}


