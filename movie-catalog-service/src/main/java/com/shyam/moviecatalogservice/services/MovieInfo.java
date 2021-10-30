package com.shyam.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shyam.moviecatalogservice.models.CatalogItem;
import com.shyam.moviecatalogservice.models.Movie;
import com.shyam.moviecatalogservice.models.Rating;

@Service
public class MovieInfo {

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * for bulkhead pattern
	 * @HystrixCommand(fallbackMethod = "getAssociatedInstitutionFallback",
	 * threadPoolKey = "threadPoolInstitution", threadPoolProperties = {
	 * 
	 * @HystrixProperty(name = "coreSize", value = "15"),
	 * 
	 * @HystrixProperty(name = "maxQueueSize", value = "5") })
	 */
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}

	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie Name Not Found", "", rating.getRating());
	}
}
