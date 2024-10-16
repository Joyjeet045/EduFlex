package com.example.my_project.service;

import com.example.my_project.dao.ChannelDao;
import com.example.my_project.models.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChannelService {
  private final ChannelDao channelDao;

  @Autowired
  public ChannelService(ChannelDao channelDao) {
    this.channelDao = channelDao;
  }

  public void createChannel(Long courseId, String name) {
    Channel channel = new Channel();
    channel.setCourseId(courseId);
    channel.setName(name);
    channelDao.save(channel);
  }

  public List<Channel> getChannelsByCourseId(Long courseId) {
    return channelDao.findByCourseId(courseId);
  }
  public Channel getChannelById(Long id) {
    return channelDao.findById(id);
  }
}