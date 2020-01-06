package info.svetlik.pia.service;

import java.util.List;

import info.svetlik.pia.domain.Commodity;

public interface CommodityManager {
	
	List<Commodity> getCommodities();

	void addCommodity(String name, double fullPrice);
	void addCommodity(String name, double fullPrice, double reductedPrice);

	int getVAT();

}
