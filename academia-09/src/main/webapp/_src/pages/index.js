import Head from 'next/head';
import SignIn from '../components/login/SignIn';
import { Container } from '@mui/material';
import * as React from "react";
import Link from 'next/link';

export default function Home() {
  return (
    <Container>
      <Head>
        <title>Academia</title>
      </Head>
      <SignIn />
    </Container>
  )
}
