package com.ironrhino.common.support;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.ironrhino.core.mail.MailSender;
import org.ironrhino.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;

@Singleton
@Named
public class Backup {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private MailSender mailSender;

	@Value("${backup.path:}")
	private String path;
	
	@Value("${backup.email:}")
	private String email;

	@Scheduled(cron = "0 0 22 * * ?")
	public void send() {
		if (StringUtils.isBlank(path))
			return;
		String realpath = eval(path);
		File f = new File(realpath);
		if (!f.exists())
			return;
		try {
			final File attachment = FileUtils.zip(f);
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage)
						throws MessagingException {
					MimeMessageHelper message = new MimeMessageHelper(
							mimeMessage, true, "UTF-8");
					message.setFrom(email);
					message.setTo(email);
					message.setSubject("backup");
					message.setText("", true);
					message.addAttachment(attachment.getName(), attachment);
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static String eval(String path) {
		int i = path.indexOf('{');
		if (i < 0)
			return path;
		int j = path.indexOf('}');
		String s1 = path.substring(0, i);
		String pattern = path.substring(i + 1, j);
		String s2 = path.substring(j + 1);
		return eval(new StringBuilder(s1)
				.append(new SimpleDateFormat(pattern).format(new Date()))
				.append(s2).toString());
	}

}
