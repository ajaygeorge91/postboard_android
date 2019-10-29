package online.postboard.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PaginatedResult<ITEM> implements Serializable {
	private List<ITEM> result;
	private int pageNumber;
	private int numberOfRecords;
	private String orderBy;
	private int totalCount;

	public void setResult(List<ITEM> result){
		this.result = result;
	}

	public List<ITEM> getResult(){
		return result;
	}

	public void setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
	}

	public int getPageNumber(){
		return pageNumber;
	}

	public void setNumberOfRecords(int numberOfRecords){
		this.numberOfRecords = numberOfRecords;
	}

	public int getNumberOfRecords(){
		return numberOfRecords;
	}

	public void setOrderBy(String orderBy){
		this.orderBy = orderBy;
	}

	public String getOrderBy(){
		return orderBy;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}

	@Override
	public String toString(){
		return
				"PaginatedResult{" +
						"result = '" + result + '\'' +
						",pageNumber = '" + pageNumber + '\'' +
						",numberOfRecords = '" + numberOfRecords + '\'' +
						",orderBy = '" + orderBy + '\'' +
						",totalCount = '" + totalCount + '\'' +
						"}";
	}

}