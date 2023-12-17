package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportError<T> : (Any) -> Output<T>