package de.mrfrey.adsbmonitor.ui.position

import java.util.stream.Stream


interface PositionRepositoryCustom {
    Stream<Position> getPositions(Long begin, Long end)
}