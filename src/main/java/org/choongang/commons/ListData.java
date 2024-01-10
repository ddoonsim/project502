package org.choongang.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 데이터 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> {
    private List<T> items ;
    private Pagination pagination ;
}
