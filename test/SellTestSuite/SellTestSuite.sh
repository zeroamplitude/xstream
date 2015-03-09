#!/usr/bin/env bash

SDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

SellTestSuite() {
      bash ${SDIR}/ValidSellTestCase/ValidSellTestCase.sh
      bash ${SDIR}/InvalidSellTestCaseA/InvalidSellTestCaseA.sh
      bash ${SDIR}/InvalidSellTestCaseB/InvalidSellTestCaseB.sh
      bash ${SDIR}/InvalidSellTestCaseC/InvalidSellTestCaseC.sh
      bash ${SDIR}/InvalidSellTestCaseD/InvalidSellTestCaseD.sh
}


SellTestSuite
