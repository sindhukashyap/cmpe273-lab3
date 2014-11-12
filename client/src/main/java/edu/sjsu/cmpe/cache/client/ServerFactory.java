package edu.sjsu.cmpe.cache.client;
import edu.sjsu.cmpe.cache.client.DistributedCacheService;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashFunction;
import com.google.common.hash.HashCode;
import com.google.common.base.Charsets;

public class ServerFactory<T extends CacheServiceInterface>

{

	   //private final HashFunction hashFunction;
  	private final int numberOfReplicas;
  	private final SortedMap<Integer,T> circle = new TreeMap<Integer, T>();


	public ServerFactory(int numberOfReplicas, Collection<T> nodes)
	{
		  //this.hashFunction = hashFunction;
    	this.numberOfReplicas = numberOfReplicas;

    	addAll(nodes);
	}

  public ServerFactory(int numberOfReplicas)
  {
    this.numberOfReplicas = numberOfReplicas;
  }

  public void addAll(Collection<T> servers)
  {
      for(T node : servers)
      {
          add(node);
      }
  }

	public void add(T node) 
	{
    	for (int i = 0; i < numberOfReplicas; i++) 
    	{
	  		//circle.put(node.hashCode() + i,node);
        String n = ((DistributedCacheService)node).getCacheServerUrl();
        //String n = node.getCacheServerUrl();
        //System.out.println("The node is: "+ n);
        int hash = Hashing.md5().hashString(n,Charsets.UTF_8).asInt();
        circle.put(hash,node);
        //System.out.println("What is this "+ hash.asInt());
        //int nodeString = Hashing.md5().hashString(node,Charsets.UTF_8);
        //System.out.println("Node String= " + nodeString);
        
        //System.out.println("Hash of node= " + node.hashCode() +",  node is "+node);
        //circle.put(hashFunction.hash(node.toString() + i),node);
    	}
      
  }

  // 	public void remove(T node) 
  // {
  //   for (int i = 0; i < numberOfReplicas; i++) 
  //   {
  //      circle.remove(Hashing.md5().hashString(node.cacheServerUrl,Charsets.UTF_8));
  //      //circle.remove(hashFunction.hash(node.toString() + i));
  //   }
  // }

  public T getServer(Integer key)
  {
  	if(circle.isEmpty())
  	{
  		return null;
  	}
  	//int hash = hashFunction.hash(key);
    int hash = key.hashCode();
    System.out.println("hash of the key is "+hash);
  	if(!circle.containsKey(hash))
  	{
  		SortedMap<Integer, T> tailMap = circle.tailMap(hash);
  		if(tailMap.isEmpty())
  		{
  			hash = circle.firstKey();
  		}
  		else
  		{
  			hash = tailMap.firstKey();
  		}
  	}
	return circle.get(hash);
  }

  public void printCircle()
  {
      for(Integer hash : circle.keySet())
      {
        System.out.println("Hash = " + hash +"  node is "+circle.get(hash));
      }
  }


}


