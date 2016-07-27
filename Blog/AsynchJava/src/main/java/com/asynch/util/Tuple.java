package com.asynch.util;

public final class Tuple<A, B> {

	private final A _1;
	private final A _2;

	public Tuple(A _1, A _2) {
		super();
		this._1 = _1;
		this._2 = _2;
	}

	public A get_1() {
		return _1;
	}

	public A get_2() {
		return _2;
	}

}
