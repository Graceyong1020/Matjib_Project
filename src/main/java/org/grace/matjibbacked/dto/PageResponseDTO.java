package org.grace.matjibbacked.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
public class PageResponseDTO<E> {

    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current; // 전체 데이터 수, 이전 페이지, 다음 페이지, 전체 페이지 수, 현재 페이지

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount){

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;

        this.totalCount = (int) totalCount;

        //endPage 계산
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        //startPage 계산
        int start = end - 9;

        //실제 마지막 페이지 계산
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        end = end > last? last : end; // end가 last보다 크면 last로 변경

        this.prev = start > 1; // start가 1보다 크면 이전 페이지로 이동 가능
        this.next = totalCount > end * pageRequestDTO.getSize(); // 전체 데이터 수가 end * size보다 크면 다음 페이지로 이동 가능

        //현재 페이지 계산
       this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

       //이전 페이지 계산
        this.prevPage = prev ? start + 1 : 0;
        //다음 페이지 계산
        this.nextPage = next ? end + 1 : 0;

    }
}
