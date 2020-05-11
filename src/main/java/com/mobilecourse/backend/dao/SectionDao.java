package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Section;

import java.util.List;

public interface SectionDao {

	Section getBySectionId(int section_id);

	Section getBySectionName(String section_name);

	List<Section>  selectByCategory(String category);

	void updateArticleCnt(int section_id, int update_num);

	void updateFollowerCnt(int section_id, int update_num);
}
