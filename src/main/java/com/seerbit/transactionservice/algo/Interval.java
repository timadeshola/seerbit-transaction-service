package com.seerbit.transactionservice.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/24/23
 * Time: 12:24 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Interval {
    public int start;
    public int end;
}
