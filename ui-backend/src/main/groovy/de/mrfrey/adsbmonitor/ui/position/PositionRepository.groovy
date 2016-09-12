package de.mrfrey.adsbmonitor.ui.position

import org.bson.types.ObjectId
import org.springframework.data.repository.Repository


interface PositionRepository extends Repository<Position, ObjectId>, PositionRepositoryCustom {

}