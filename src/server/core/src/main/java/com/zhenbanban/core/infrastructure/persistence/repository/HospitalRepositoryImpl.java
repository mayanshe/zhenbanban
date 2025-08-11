package com.zhenbanban.core.infrastructure.persistence.repository;

import com.zhenbanban.core.domain.institutioncontext.entity.Hospital;
import com.zhenbanban.core.domain.institutioncontext.repository.HospitalRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.HospitalConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.HospitalPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.HospitalPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import com.zhenbanban.core.shared.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HospitalRepositoryImpl implements HospitalRepository {

    private final HospitalPoMapper hospitalPoMapper;

    @Autowired
    public HospitalRepositoryImpl(@Lazy HospitalPoMapper hospitalPoMapper) {
        this.hospitalPoMapper = hospitalPoMapper;
    }

    @Override
    public Hospital load(Long id) {
        HospitalPo po = hospitalPoMapper.selectById(id);
        if (po == null) {
            throw new ResourceNotFoundException(String.format("医院ID %d 不存在", id));
        }
        return HospitalConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Hospital aggregate, boolean isNew) {
        HospitalPo po = HospitalConverter.INSTANCE.toPo(aggregate);
        if (isNew) {
            if (hospitalPoMapper.insert(po) <= 0) {
                throw new ServiceUnavailableException(String.format("医院 `%s` 新增失败", aggregate.getHospitalName()));
            }
        } else {
            if (hospitalPoMapper.updateById(po) <= 0) {
                throw new ServiceUnavailableException(String.format("医院 `%s` 更新失败", aggregate.getHospitalName()));
            }
        }
        return po.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        hospitalPoMapper.deleteById(id);
    }
}
