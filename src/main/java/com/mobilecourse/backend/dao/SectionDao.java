package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Section;

public interface SectionDao {

	Section getBySectionId(int section_id);

	Section getBySectionName(String section_name);

}
