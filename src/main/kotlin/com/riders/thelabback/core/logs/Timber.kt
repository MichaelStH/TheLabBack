package com.riders.thelabback.core.logs

import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.ISeedling


typealias Timber = Arbor
typealias Tree = ISeedling

fun plant(tree: Tree) = Timber.sow(seedling = tree)