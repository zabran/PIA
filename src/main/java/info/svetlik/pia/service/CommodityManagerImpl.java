package info.svetlik.pia.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import info.svetlik.pia.dao.CommodityRepository;
import info.svetlik.pia.domain.Commodity;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommodityManagerImpl implements CommodityManager {

	private final CommodityRepository commodityRepo;
	private static final double FULLTOREDUCTED = 0.8;

	@Autowired
	public CommodityManagerImpl(CommodityRepository commodityRepo) {
		this.commodityRepo = commodityRepo;
	}

	@EventListener(classes = {
			ContextRefreshedEvent.class
	})
	@Order(1)
	public void setup() {
		if (this.commodityRepo.count() == 0) {
			log.info("No commodity present, creating some examples.");
			this.addCommodity("PIA Web", 2499);
			this.addCommodity("PIA Lesson (hour price)", 249);
			this.addCommodity("Additional PIA Exam Term", 599);
			this.addCommodity("PIA Exam point", 99);
		}
	}

	@Override
	public List<Commodity> getCommodities() {
		List<Commodity> retVal = new LinkedList<>();
		this.commodityRepo.findAll().forEach(retVal::add);
		return retVal;
	}

	@Override
	public void addCommodity(String name, double fullPrice) {
		addCommodity(name, fullPrice, fullPrice*FULLTOREDUCTED);
	}

	@Override
	public void addCommodity(String name, double fullPrice, double reductedPrice) {
		Commodity com = new Commodity(name, fullPrice, reductedPrice);//TODO check name unique
		this.commodityRepo.save(com);
		
	}

	@Override
	public int getVAT() {
		return (int)( Math.round(((1/CommodityManagerImpl.FULLTOREDUCTED)-1)*100));
	}
	
	//TODO update price

}
